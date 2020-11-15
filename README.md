# distributed-job-spike

This spike, explore hot use messaging to coordinate distributed jobs along a horizontally scaled system.
In order to test this application it is required a kubernetes installation, minikube will be enough.

Supposing that minikube will be your testing choice, it was mine, after the installation, 
is required create in your AWS account a user that is capable to interacts with SQS and create a queue, 
for my case I created a fifo queue that especially in job case I perceive that it provides more safety in delivery, 
maybe the messaging order matter for your job and in any case I do not want duplicated messages and FIFO queue works better 
in this scenario rather than standard queues.

After that you created IAM user and FIFO queue you should  replace those data in kubernetes/secrets.yml, pay attention to deploy 
secrets.yml before the other .yml file and that it. 
    
## k8s image stuff.

This time I do not had pushed those docker images on my docker hub you should build by your self, but it is very simple.

First thing open a shell and type the command  `eval $(minikube docker-env)` in this way you are configuring your environment to use minikube’s Docker daemon. 
Use the same shell to build the image for `distributed-job` and `job-publisher`, doing so,the image will be built in the same docker registry of minikube. 
Therefore, make your shell under `distributed-job` and type `docker build -t mrflick72/distributed-job:latest .` then under `job-publisher` type `docker build -t mrflick72/job-scheduler:latest .`.

After that you wll have your image on a docker registry that your minikube installation is capable to see and pull images. Go under kubernetes folder and type those commands in sequence:
- kubectle apply -f secrets.yml
- kubectle apply -f job-scheduler-consumer.yml
- kubectle apply -f job-scheduler-publisher.yml

Going in tail of the all consumer replicas you can see that only one of the three replicas will print the message `job schedule` that is the eco that our consumer print form the message published by the publisher python agent scheduled as a k8s cron job.