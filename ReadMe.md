<h2>Problem :</h2>
You are provided with hotels database in CSV (Comma Separated Values) format.

We need you to implement HTTP service, according to the API requirements described below. You may use any language or platform that you like: C#/Java/Scala/etc.

RateLimit: API calls need to be rate limited (request per 10 seconds) based on API Key provided in each http call.
 On exceeding the limit, api key must be suspended for next 5 minutes.
Api key can have different rate limit set, in this case from configuration, and if not present there must be a global rate limit applied.
Search hotels by CityId
Provide optional sorting of the result by Price (both ASC and DESC order).


<b>Note:</b> Please don’t use any external library or key-value store for RateLimit. You need to create a InMemory Implementation for RateLimit.
<h2>Solution :</h2>
<h5>Here I'm using jax-rs for rest service exposure and jersy rest for testing rest service
Apart from that here i'm using spring AOP to avoid boiler plate code
 in case there are other API which also need same API implementation
 just apply <code> @RateLimiter</code> ovet method then this method
 use same rate limit api</h5>

 For storage purpose I've desinged my own hash map like structure <code> APIStorage </code> which can be right now access time is <Code> <b>O(n)</b></code>
 which can be Refactor  to  <Code> <b>O(1)</b></code> if we have some hashin technique which can generate unique index based on API key <h6>{unique api key}</h6>

 As there can be multiple thread hiting the API so I'm using <Code>AtomicInteger</Code> to over come this problem.


 <h5>Approach for API Key Blockage:</h5>
 1. if time diff is less than 10 and api count limit  is equal to Threshold count
 2. Block api key for 5 min and throw exception

 3. else if check already blocked

    3.1  no : -> update last access time and increment counter

    3.2 yes : -> check can we unblock;

      3.2.1 no : -> do nothing and throw exception
      3.2.2 yes: -> reinitialize counter and update last update time

 4. else Not blocked: update last access time and increment counter.


<i>Default blockage time is 5 min. So to test functionality use lower value as thread will sleep for 5 min if you run test case</i>
 <h2>To test first deploy application and run <code>mvn test</code></h2>
     
