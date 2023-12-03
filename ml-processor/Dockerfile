FROM python:3
WORKDIR /app
COPY . /app
RUN pip install scikit-learn
RUN pip install -r requirements.txt
RUN pip install joblib==1.3.2
EXPOSE 3000
CMD python ml-processor.py
