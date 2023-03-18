Custom reactive implementation for simple reactive return types:
T       -> fun : Mono<ResponseEntity<T>>
List<T> -> fun : Mono<ResponseEntity<List<T>>>

https://raw.githubusercontent.com/OpenAPITools/openapi-generator/v6.4.0/modules/openapi-generator/src/main/resources/kotlin-spring/apiInterface.mustache
https://raw.githubusercontent.com/OpenAPITools/openapi-generator/v6.4.0/modules/openapi-generator/src/main/resources/kotlin-spring/returnValue.mustache
