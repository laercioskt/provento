#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

echo "=================================================================="
echo "==== WHAT IS THE HEROKU APP NAME?"
read herokuapp

echo "=================================================================="
echo "==== WHAT IS THE LOCAL DATABASE NAME?"
read databasename

echo "=================================================================="
echo "==== CREATING ${herokuapp} DUMP ..."
heroku pg:backups:capture --app $herokuapp

echo "=================================================================="
echo "==== DOWNLOADING ${herokuapp} DUMP ..."
heroku pg:backups:download --app $herokuapp

echo "=================================================================="
echo "==== CREATING DB ${databasename} LOCALLY (DROP IF NECESSARY) ..."
psql -h localhost -d postgres -U postgres -p 5432 -a -w -f $DIR/DropAndCreateDB.sql -v databasename=$databasename

echo "=================================================================="
echo "==== RESTORING DUMP ON ${databasename} AND THEN REMOVE IT ..."
pg_restore --clean --verbose --no-acl --no-owner -h localhost -U postgres -d "${databasename}" latest.dump
rm latest.dump

echo "=================================================================="
echo "==== ... FINISHED"