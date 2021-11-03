# spring-boot-with-metrics

This demo app shows how a Spring Boot application can expose a Prometheus metrics endpoint for scraping.

## To run

To run the app:

    mvn clean spring-boot:run
    
This will expose Prometheus metrics at `/actuator/prometheus`. This is a simple `key value` listing of metrics.
    ...    

## Timing a custom method

The method in the application which responds to the REST request has been annotated with `@Timed`, so Micrometer will capture the execution time of this method:

```java
@GetMapping("/greeting")
@Timed(value = "greeting.time", description = "Time taken to return greeting",
        percentiles = {0.5, 0.90})
public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
}
```

Once you make a request to the service at <http://localhost:8080/greeting>, you will also see a new metric exposed, `greeting_time_seconds` exposed, which shows the execution time of the `greeting` method:

    # HELP greeting_time_seconds Time taken to return greeting
    # TYPE greeting_time_seconds summary
    greeting_time_seconds{class="com.tutorialworks.demos.springbootwithmetrics.GreetingController",exception="none",met
    hod="greeting",quantile="0.9",} 0.02097152
    greeting_time_seconds_count{class="com.tutorialworks.demos.springbootwithmetrics.GreetingController",exception="non
    e",method="greeting",} 1.0
    greeting_time_seconds_sum{class="com.tutorialworks.demos.springbootwithmetrics.GreetingController",exception="none"
    ,method="greeting",} 0.021689345
    # HELP greeting_time_seconds_max Time taken to return greeting
    # TYPE greeting_time_seconds_max gauge
    greeting_time_seconds_max{class="com.tutorialworks.demos.springbootwithmetrics.GreetingController",exception="none",method="greeting",} 0.021689345



> All implementations of _Timer_ report at least the total time and count of events as separate time series.

## Getting metrics into Prometheus

Now we need to get these metrics into Prometheus.

In another terminal, use Docker to pull the Prometheus image from the Docker Hub using

	docker pull prom/prometheus

And run the Docker container using:

	docker run -d -p 9090:9090 -v [your path to prometheus.yml]:/etc/prometheus/prometheus.yml prom/prometheus
    eg: docker run -d -p 9090:9090 -v C:/Development_Avecto/Micrometer-Prometheus/spring-boot-with-metrics/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
        
Check the Prometheus console at <http://localhost:9090>.

- Go to _Targets_, you should see the Spring Boot app being scraped successfully.

- On the _Graph_ page, you should be able to type in a metric from the application (e.g. `tomcat_sessions_active_current_sessions`, or `greeting_time_seconds`) and see the raw data, or plot a graph

