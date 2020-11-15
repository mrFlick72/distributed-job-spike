import os
import uuid
import boto3

sqsClient = boto3.client('sqs')

def publish():
    payload = os.getenv("PAYLOAD")
    sqsClient.send_message(
        MessageDeduplicationId=f"{uuid.uuid4()}",
        MessageGroupId=f"{uuid.uuid4()}",
        QueueUrl=os.getenv("QUEUE_URL"),
            MessageBody=payload
    )


if __name__ == '__main__':
    publish()
