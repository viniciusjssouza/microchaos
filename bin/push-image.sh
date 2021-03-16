docker build ./ -t microchaos:latest
docker tag microchaos:latest viniciusjssouza/microchaos:latest
docker login
docker push viniciusjssouza/microchaos:latest