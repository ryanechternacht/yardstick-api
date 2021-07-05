FROM clojure:openjdk-17-tools-deps
COPY . /usr/src/app
WORKDIR /usr/src/app
# CMD ["sh", "-c", "sleep 1 && exec clj"]
CMD ["sh", "-c", "sleep 1 && exec clj -m yardstick-api.server"]