package microchaos.service.spec

import microchaos.service.spec.model.*

object SampleServices {
    val simpleService = ServiceSpec(
        Service(
            name = "simpleService",
            type = "web",
            port = 8080,
            endpoints = arrayListOf(
                Endpoint(
                    path = "/some-io",
                    method = "get",
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "ioBounded",
                                distribution = Distibution(type = "logNormal", mean = 200.0, stdDeviation = 500.0)
                            )
                        ),
                        response = arrayListOf(
                            Response(status = 200, content = "success")
                        )
                    )
                ),
                Endpoint(
                    path = "/unexpected-response",
                    method = "get",
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "cpuBounded",
                                distribution = Distibution(type = "logNormal", mean = 200.0, stdDeviation = 500.0)
                            )
                        ),
                        response = arrayListOf(
                            Response(status = 200, probability = 80.0),
                            Response(status = 500, probability = 20.0)
                        )
                    )
                ),
                Endpoint(
                    path = "/google",
                    method = "post",
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "cpuBounded",
                                distribution = Distibution(type = "logNormal", mean = 200.0, stdDeviation = 500.0)
                            ),
                            Execution(
                                type = "request",
                                httpRequest = HttpRequest(
                                    method = "get",
                                    target = "https://www.google.com/complete/search?q=vai&cp=3&client=psy-ab&pq=vai"
                                )
                            )
                        ),
                        response = arrayListOf(
                            Response(status = 200, probability = 80.0),
                            Response(status = 500, probability = 20.0)
                        )
                    )
                )
            )
        )
    )
}