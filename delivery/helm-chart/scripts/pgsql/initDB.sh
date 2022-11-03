# INIT PGSQL BY ALTERING PUBLIC SCHEMA
# REPLACE VARIABLES WITH ONE FROM GITLAB CI

set -e

psql -v ON_ERROR_STOP=1 --username "$CLOUD_POSTGRES_ROOT_USER" --dbname "$CLOUD_POSTGRES_ROOT_DB" <<-EOSQL
    DROP SCHEMA public;
EOSQL