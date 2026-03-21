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
##### Approach
- For large PDFs above a threshold, the backend would:
    - inspect the PDF
    - split it into valid mini-PDF chunks by page range (pages 1–25 → chunk 1, pages 26–50 → chunk 2 ...)
    - generate a manifest describing the chunks
    - store chunk metadata
    - serve the viewer chunk-by-chunk instead of whole-file-first
- The viewer initially requested for 
    - ?
- Now it requests for only
    - manifest
    - first chunk
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
- Instead of returning the raw PDF directly to the viewer, the backend first returned a manifest
