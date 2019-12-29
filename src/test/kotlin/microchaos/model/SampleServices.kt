package microchaos.model

import microchaos.model.*

object SampleServices {
    val simpleService = ServiceModel(
        Service(
            name = "simpleService",
            type = "web",
            port = 8080,
            endpoints = arrayListOf(
                Endpoint(
                    path = "/some-io",
                    method = "get",
                    behavior = Behavior(
                        commands = arrayListOf(
                            Command(
                                type = "ioBounded",
                                duration = Distribution(type = "logNormal", mean = 6.0, stdDeviation = 0.25)
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
                        commands = arrayListOf(
                            Command(
                                type = "cpuBounded",
                                duration = Distribution(type = "logNormal", mean = 6.0, stdDeviation = 0.25)
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
                        commands = arrayListOf(
                            Command(
                                type = "cpuBounded",
                                duration = Distribution(type = "logNormal", mean = 6.0, stdDeviation = 0.25)
                            ),
                            Command(
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

    val periodicTasks = ServiceModel(
        Service(
            name = "periodicTasks",
            type = "web",
            port = 8080,
            periodicTasks = listOf(
                PeriodicTask(
                    period = 5000,
                    behavior = Behavior(
                        listOf(Command(
                            type = "networkFailure",
                            duration = Distribution(
                                type = "constant",
                                mean = 10_000.0
                            )
                        ))
                    )
                ),
                PeriodicTask(
                    period = 50_000,
                    behavior = Behavior(
                        listOf(Command(
                            type = "terminateProcess"
                        ))
                    )
                )
            )
        )
    )
}