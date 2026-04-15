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
- I was working on a travel booking platform in the Expedia ecosystem as part of a vendor team. One part of the workflow involved post-payment booking reconciliation.
Normally the flow was:
customer makes payment
payment succeeds
downstream reservation/booking confirmation happens
booking status is updated
But occasionally, due to downstream timeouts or partial failures, some bookings got stuck in an intermediate state like:
PAYMENT_SUCCESS
BOOKING_PENDING
These cases were rare, but they were important because they affected customer trust and support load.
The product team needed a lightweight background mechanism to periodically detect such stuck bookings and trigger reconciliation logic.
The frequency was low:
every 10 or 15 minutes was enough
expected record count each run was small
not a high-throughput streaming problem

---
- STAR - Situation, Task, Action, Result
- confidence, positivity, humble 
- Leadership - taking initiatives
- Resilience - rising up to challenges, talk about accomplishment
- Teamwork -
- Influence/Persuasion
- Ethics/Morality/Integrity
- Give safe answers
