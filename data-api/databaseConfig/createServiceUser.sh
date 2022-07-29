
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    select ('base de données démarrée') as status;
    CREATE SCHEMA IF NOT EXISTS msp;
EOSQL

