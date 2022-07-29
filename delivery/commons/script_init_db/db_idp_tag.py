
#!/usr/bin/python3
#
#
import psycopg2
import sys
# Connect to the PostgreSQL server using db parameters and CI variables


def connect(db):
    conn = None
    args = sys.argv

    print('Connecting to the PostgreSQL database...')

    # TODO variabiliser avec gitlabci
    conn = psycopg2.connect(
        host=str(args[2]),
        database=str(args[3]),
        user=str(args[4]),
        password=str(args[5]),
    )

    print('Connected to the PostgreSQL database.')

    return conn

# Disconnect the current connection


def disconnect(conn):
    conn.close()

# Create db using tag and current connection
# Returns true if db created else false


def create_database(conn, db_name_tag):

    # constant use to return created or not db
    is_db_created = False

    # enable autocommit to prevent transactions
    conn.autocommit = True

    # create a cursor
    cur = conn.cursor()

    print('PostgreSQL check existing database:')
    cur.execute(
        "SELECT datname FROM pg_database WHERE datname = '{}';".format(db_name_tag))

    # Get database
    db_version_tag_exists = cur.fetchone()

    if not db_version_tag_exists:
        print("'{}' Database does not exist.".format(db_name_tag))
        cur.execute("CREATE DATABASE {};".format(db_name_tag))
        print("'{}' Database created.".format(db_name_tag))
        is_db_created = True
    else:
        print("'{}' Database already exist".format(db_name_tag))

    # Restore transactions
    conn.autocommit = False

    return is_db_created

# Create schema for current connection and db
# Update search path
# Grant read access to schema and tables


def manage_schema_user(conn, db_name_tag, schema_name, readaccess_role):

    # enable autocommit to prevent transactions
    conn.autocommit = True

    # create a cursor
    cur = conn.cursor()
    print('PostgreSQL create schema')

    cur.execute("CREATE SCHEMA IF NOT EXISTS {};".format(schema_name))

    print("'{}' Schema created.".format(schema_name))

    print('PostgreSQL update search_path')

    cur.execute("ALTER DATABASE {} SET search_path TO {};".format(
        db_name_tag, schema_name))

    print("'{}' Schema path updated.".format(schema_name))

    print('PostgreSQL Grant restrict access to {}'.format(readaccess_role))

    # query to grant connect to db
    cur.execute("GRANT CONNECT ON DATABASE {} TO {};".format(
        db_name_tag, readaccess_role))

    # query to grant schema usage
    cur.execute("GRANT USAGE ON SCHEMA {} TO {};".format(
        schema_name, readaccess_role))

    # query to grant only select
    cur.execute("ALTER DEFAULT PRIVILEGES IN SCHEMA {} GRANT SELECT ON TABLES TO {};".format(
        schema_name, readaccess_role))

    print("Grant access added to {}".format(readaccess_role))

    # Restore transactions
    conn.autocommit = False


def createDatabaseForTag(tag):
    conn = None

    # constant db name to create new db from
    db_name = 'postgres'

    # new db name to create for tag
    db_name_tag = 'keycloak_{}'.format(tag)
    # constant schema name #TODO CI
    schema_name = 'idp_db'

    # constant api user name
    readaccess_role = 'readaccess'

    is_db_created = False

    try:
        conn = connect(db_name)
        is_db_created = create_database(conn, db_name_tag)
        disconnect(conn)
        if is_db_created:
            conn = connect(db_name_tag)
            manage_schema_user(conn, db_name_tag, schema_name, readaccess_role)
            disconnect(conn)
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if conn is not None:
            disconnect(conn)
            print('Database connection closed.')


if __name__ == '__main__':
    createDatabaseForTag(sys.argv[1])
