// package dev.sluka.movies.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// import dev.sluka.movies.Service.SampleService;

// /**
//  * Controller class to define REST endpoints for testing the Circuit Breaker.
//  */
// @RestController
// public class CircuitBreakerController {

//     private final SampleService sampleService;

//     public CircuitBreakerController(SampleService sampleService) {
//         this.sampleService = sampleService;
//     }

//     /**
//      * Endpoint to test the Circuit Breaker.
//      * @return Response from SampleService
//      */
//     @GetMapping("/test")
//     public String testCircuitBreaker() {
//         return sampleService.callExternalService();
//     }
// }
