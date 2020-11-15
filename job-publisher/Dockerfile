FROM python:3.8

RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && \
    python3.8 get-pip.py && \
    pip install boto3

COPY publisher.py publisher.py

CMD ["python3.8", "publisher.py"]