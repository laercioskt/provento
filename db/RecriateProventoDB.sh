#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

cd "$DIR" || exit

echo "CREATING DB PROVENTO..."
psql -h localhost -d postgres -U postgres -p 5432 -a -w -f DropAndCreateDB.sql
echo "DB PROVENTO CREATED."