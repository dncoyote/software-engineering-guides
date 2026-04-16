# Behavioral
## Tell me about yourself

## Why should we hire you 

## What do you know about this company? 

## Strength and Weakness 

## What motivates you at work? 

## Have you worked in a Leadership position? 

## Why are you looking for a change?  

## What are your long term goals? 

## Tell me about your current project?
- Agile or Scrum

## Day-to-day activities? 

## Rate yourself on this skill 

## Working during weekends during Delivery? 

## How do you handle when requirements change in the last moment? 

## How do you manage conflict with your colleagues? 

## Are you willing to learn new technology? 

## Describe a situation when you faced tight deadline and high pressure and how did you handle it?

## Do you have any questions for me? 

# Challenges
## Large PDF Viewer Performance Issue Solved in Backend Java
### Context
- I was working on a document management system used internally by enterprise operations teams
- The product stored and displayed large business documents such as:
    - contracts
    - employee records
    - compliance reports
    - scanned archival PDFs
- Viewer was embedded in the web application, so users did not download the file first. 
- They expected the document to open quickly in-browser and let them start reading immediately.
- This mattered because these documents were part of business workflows. If the viewer took too long to load, users would refresh repeatedly and open multiple tabs.
- The issue disproportionately affected very large PDFs, especially scanned documents with hundreds of pages.
### Problem
- We started seeing repeated complaints that large PDFs were effectively unusable in the viewer.
- First render could take 8–20 seconds
- Browser tab memory usage became very high
- Users often saw a blank or frozen viewer before the first page appeared
- Retries increased server load because users refreshed repeatedly
- The real problem was that our viewing flow assumed the entire PDF should be served and consumed as one unit, which worked when documents were small but broke down for large scanned files.
### Explanation
#### Flow
- User opens a document
- Frontend requests /documents/{id}/content
- Spring Boot service streams the entire PDF
- Browser-side viewer loads and renders it
#### Hypothesis
- At first glance, it looked like a network issue.
- But when testing with heavy pdfs we were able to recreate the issue.
- The browser was downloading a large file before showing useful content
- Time to first visible page was much worse than pure network transfer time
- Large scanned PDFs were the worst
- We needed to optimize time-to-first-page and incremental access.
- We could not just split raw PDF bytes arbitrarily.
- If we wanted chunking, it had to be done as valid logical page bundles.
- That led us away from naive HTTP byte chunking
#### Solution
- On upload or first access, preprocess large PDFs in Java into page-based chunks, store those chunks separately, generate metadata, and serve the viewer through a manifest-driven progressive loading model.
- We generated a manifest for the viewer and stored chunk artifacts in object storage. That allowed the frontend to request only the first chunk initially and then load the rest progressively as the user navigated.
- We also added asynchronous preprocessing, chunk metadata tracking, and fallback behavior for unprocessed or failed documents. The result was that time to first visible page dropped dramatically and the viewer felt responsive even for documents with hundreds of pages.
##### Approach
- For large PDFs above a threshold, the backend would:
    - inspect the PDF
    - split it into valid mini-PDF chunks by page range (pages 1–25 → chunk 1, pages 26–50 → chunk 2 ...)
    - generate a manifest describing the chunks
    - store chunk metadata
    - serve the viewer chunk-by-chunk instead of whole-file-first
- The viewer initially requested for 
```
GET /documents/{id}/content
→ returns full PDF (streamed)
```
- Now it requests for only
    - manifest
    - first chunk

```
GET /documents/{id}/viewer-manifest

{
  "totalPages": 312,
  "chunkSize": 25,
  "chunks": [
    { "index": 0, "url": "/documents/123/chunks/0" },
    { "index": 1, "url": "/documents/123/chunks/1" }
  ]
}
```
- Then additional chunks were fetched sequentially as the user scrolled.
- We went with a backend preprocessing because:
    - the browser was still being forced to deal with very large source documents
    - we wanted predictable page-group artifacts
    - we needed a reusable solution for all clients
    - we could amortize preprocessing cost once and reuse the result many times
    - we wanted storage/CDN caching at the chunk level
##### Backend preprocessing pipeline
- Preprocessing was triggered:
    - during upload for new large documents, or
    - lazily on first open for older legacy documents
- Using a Java PDF library such as Apache PDFBox, the service would:
    - load document metadata
    - count pages
    - split into page ranges
    - save each range as a valid PDF
    - persist a manifest   
- Instead of returning the raw PDF directly to the viewer, the backend first returned a manifest.
- This changed the viewer behavior from “download everything and hope” to “load first usable segment, then continue progressively”.
- The legacy document exposed states like `READY`, `PROCESSING`, `FAILED` to control viewer behavior.
- Chunk size was determined based on page count, file size and nature of pdf.
###### Sequential and Predictive loading
- Once the first chunk was loaded, the viewer fetched subsequent chunks as the user advanced.
- To reduce visible wait during reading
    - when user was in chunk N, the frontend prefetched chunk N+1
    - but the backend architecture made that possible by exposing independently retrievable valid chunks
###### Caching
- object storage/CN caching:Chunk artifacts were immutable once generated, so they were cache-friendly.
- Metadata caching: The chunk manifest was cached because it was read frequently but changed rarely.
###### Async job execution
- We avoided doing chunk generation inline during the viewer request for new large documents because that would simply shift latency rendering to backend generation.
- We a had to ensure multiple users opening the game large unprocessed document did not enqueue duplicate preprocessins jobs.
##### Other Solutions
- HTTP Byte chunking
    - HTTP allows partial file fetch like `Range: bytes=0-65536`
    - This is supported by S3/CDN and works with large files.
    - This didn't work with pdf.js
    - Unpredictable performance
- Server-side rendered page images/thumbnails + lazy fetch
    - Backend converts pages → images (PNG/JPEG)
    - Text search breaks
    - Storage explosion
- first-page image preview fast, then chunked PDF sections
#### Trade-offs
- What we optimized for
    - time to first page
    - perceived responsiveness
    - repeated-view efficiency
    - browser usability for large PDFS
- However storage was compromised, as storage usage increased
    - original PDF
    - chunked derivatives
    - manifest metadata
- More backend complexity
    - preprocessing pipeline
    - status management
    - failure handling
    - artifact lifecycle
- Eventual readiness
    - For some legacy files, there could be a short preprocessing window before the document became progressively viewable.
### Follow-ups
- Viewer: Before vs After
    - PDF.js is the De facto standard browser PDF renderer
    - Parses PDF in JS
    - Renders pages on `<canvas>`
    - With Backend preprocessing, we treated each chunk as a separate PDF document.
    - We didn’t “optimize PDF.js” — we changed the contract between backend and viewer
- What if user jumps to Page 240
    - Determine chunk - we can determine the chunk based on page number. Load and render the specific chunk.
    - We will prefetch neighboring chunks as well.
- Text Search in pdf
    - Backend Indexing - During preprocessing extract text using PDFBox, store per page text and search using Elastisearch.
- Forms, Annotations, Shared Resources
    - PDF library handled dependencies.
- How would this system behave if 100 users open the same large document simultaneously?
    - If the document is already preprocessed, the system should behave quite well.
    - If the document is not yet preprocessed, then 100 simultaneous opens can create a thundering herd problem unless we control it.
    - If All 100 users ask for `viewer-manifest` and see that there are no chunks, then it will start job to chunk 100 times
    - This was reconciled by checking the status of the document, if its `PROCESSING`
    - Also maintained a distributed lock on the `document_id`.

## Low-frequency booking audit job 
### Context
- I was working on a travel platform (Expedia ecosystem), where we handled:
    - user profiles
    - bookings
    - payments
    - logs and historical data
- Due to GDPR-like regulations, we needed to support RTBF (Right to be Forgotten) requests.
- What RTBF means here
    - all personally identifiable data must be removed or anonymized across multiple services:
        - user service
        - booking service
        - payment service
        - logs / analytics stores
### Requirement 
- We needed a periodic audit job that:
    - scans for RTBF requests in PENDING or PARTIAL state
    - retries deletion steps
    - verifies completion
    - marks request as COMPLETED or FAILED
    - alerts if stuck too long
```
rtbf_request
- request_id
- user_id
- status (PENDING, IN_PROGRESS, COMPLETED, FAILED)
- created_at
- last_attempt_at
```
- We wanted this system
    - run every 10–15 minutes
    - process small batches (50–200 records)
    - idempotent
    - safe retries
    - strong auditability
### Existing System
```
User → RTBF API → RTBF Service → downstream services
```
Problems:
    - deletion was synchronous attempt
    - failures were not retried automatically
    - state tracking existed, but no repair loop

### Initial Proposal
- Build a serverless scheduled reconciliation system
```
EventBridge Scheduler (10 min)
        ↓
     Lambda (RTBF Audit)
        ↓
 RTBF DB + downstream services
```
- Lambda responsibilities
    - fetch stale RTBF requests
    - retry deletion per service
    - update state
    - log audit trail
- Lambda and Eventbridge was ideal
    - EventBridge supports cron triggers and is fit for periodic jobs
    - independent system

### Solution
- Jenkins cluster already existed primarily used for -
    - scheduled jobs
    - operational scripts
    - maintenance workflows
- By Jenkins cluster, I mean an enterprise Jenkins setup residing in a VM with a central controller and one or more agents/executors where scheduled jobs already run.
```
Jenkins (cron every 10 min)
        ↓
 RTBF audit job (Java jar / script)
        ↓
 RTBF DB + downstream services
```
##### Flow
```
rtbf-audit-job/
  ├── src/main/java/com/company/rtbf/audit/
  │   ├── RtbfAuditApplication.java
  │   ├── RtbfAuditRunner.java
  │   ├── RtbfRequestRepository.java
  │   ├── UserDeletionClient.java
  │   ├── BookingDeletionClient.java
  │   ├── PaymentDeletionClient.java
  │   └── AuditReporter.java
  ├── src/test/java/...
  ├── pom.xml
  ├── Jenkinsfile
  ├── application-prod.yml
  └── README.md
```
- Every 10 minutes
    - query stale RTBF requests
    - process in batches
    - retry deletion
    - update status
    - log results
    - alert if failures exceed threshold
- Jenkins Pipelines can be defined as code, typically in a Jenkinsfile checked into source control, and Jenkins supports Declarative and Scripted Pipeline syntax for that.
- The business logic should be a small Java batch application or Spring Boot command-line job packaged as a jar, and Jenkins should invoke that jar.
- We ended up using Jenkins as the Delivery was quicker
```groovy
pipeline {
    agent { label 'java17-batch' }

    triggers {
        cron('H/10 * * * *')
    }

    options {
        disableConcurrentBuilds()
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Run RTBF Audit') {
            steps {
                withCredentials([
                    string(credentialsId: 'rtbf-db-url', variable: 'DB_URL'),
                    string(credentialsId: 'rtbf-api-token', variable: 'API_TOKEN')
                ]) {
                    sh '''
                      java -jar target/rtbf-audit-job.jar \
                        --batch-size=100 \
                        --cutoff-minutes=10
                    '''
                }
            }
        }
    }

    post {
        failure {
            echo 'RTBF audit job failed'
        }
    }
}
```
- We could have used a Spring Boot app with @Scheduled, and technically that would work. The reason we didn’t choose it was that this was a low-frequency audit job, not a continuously active business service. Using Spring Boot scheduling would mean introducing a new always-running JVM, new deployment and monitoring overhead, and also solving coordination if multiple instances were deployed. Since we already had Jenkins as a shared scheduler platform, it was more efficient to keep the business logic in Java but execute it as an on-demand scheduled job rather than as another permanent service.

## Concurrency Bug in Merchant Promotion Allocation System 
### Context
- I was working on a merchant promotions subsystem in a travel platform (Expedia ecosystem), responsible for applying partner-funded discounts during checkout.
- Hotels could configure campaigns like:
    - “First 300 bookings get ₹1500 cashback”
    - “Only 100 premium upgrades available”
    - “Limited weekend discount budget”
- These promotions were applied in real-time during checkout, so users saw discounted pricing before completing payment.
- This system matters as it has Direct financial impact (platform or merchant pays subsidy), Contractual limits (strict cap enforcement required)
and High visibility during peak campaigns
### Problem
- We started seeing campaign overspend issues during high-traffic promotions. If Campaign cap was 300, actual consumption was 312-327 which increased spending by 4-9%.


## Performance debugging scenario
## Cache inconsistency bug
## Distributed uploads / DICOM-scale handling 

---
- STAR - Situation, Task, Action, Result
- confidence, positivity, humble 
- Leadership - taking initiatives
- Resilience - rising up to challenges, talk about accomplishment
- Teamwork -
- Influence/Persuasion
- Ethics/Morality/Integrity
- Give safe answers
