# flash-sale-cloud

Spring Cloud MicroService practice dealing with high concurrency situations

tech stacks:
Zookeeper, Redis, MySQL, ElasticSearch, RocketMQ, Spring Cloud(Eureka,Hystrix,Zuul), Nginx

The main idea is to use redis to record how many item is available, and use RocketMQ to deal with the high cocurrent
requests in acceptable rate.

![](https://raw.githubusercontent.com/Quakiq/tinyimages/main/img202207101216075.png)

![](https://raw.githubusercontent.com/Quakiq/tinyimages/main/img202207101233699.png)
