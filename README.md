# IoTShieldDemo
Simple security app.

War app. Default data (from input.json) are analyzed on web app startup (http://localhost:8080/fromFile is called).

First analyzing step. App analyzes all requests (request and profile request) consistently. If request are called from unknown model (no profile this model), request are blocked and device quarantined. Results are printed. Before second step all data are cleared. 

Second analyzing step. App analyzes all requests (only requests, not profile requests) after all profiles data are collected. Results are printed. Intermadiate reuslts can be viewed by app requests below.

GET http://localhost:8080/fromFile - load data from input.json file. Analyze and output results.

POST http://localhost:8080/fromPost - load data from post. Send plain text like input.json content. Postman is used. 

GET http://localhost:8080/reset - clears all data. 

GET http://localhost:8080/allRequests - all registered requests.

GET http://localhost:8080/allResponses - all analyzed responses.

Exceptions (NumberFormat, FileNotFound, CastException) are possible in logs, if input data are incorrect, i.e. timestamp is text, etc.
