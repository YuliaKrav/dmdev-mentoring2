FROM postgres:14

ENV POSTGRES_DB=postgresdb
ENV POSTGRES_USER=user_db
ENV POSTGRES_PASSWORD=user_db_321
ENV LANG=en_US.utf8

EXPOSE 5432
VOLUME ["/var/lib/postgresql/data"]