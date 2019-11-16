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
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "ioBounded"
                            )
                        ),
                        response = arrayListOf(
                            Response(status = 200, content = "success")
                        )
                    )
                ),
                Endpoint(
                    path = "/unexpected-response",
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "cpuBounded"
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
                    behavior = Behavior(
                        execution = arrayListOf(
                            Execution(
                                type = "cpuBounded"
                            ),
                            Execution(
                                type = "request"
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