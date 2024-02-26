# RocksDB Reader

A plug n play service to quickly view contents from your RocksDB. All you need is the column family name and the key to fetch the value!

## Steps to Run

- Specify path to your RocksDB Database in `rocksdb-reader-server/src/main/resources/application.yml`
- Run Java Application
- Curl to fetch all Column Families: `curl 'http://localhost:8081/rocksdb/reader/v1/data/columnFamilies'`
- Curl to fetch value by column family name and key: `curl 'http://localhost:8081/rocksdb/reader/v1/data/handle/<FAMILY-NAME>/key/<KEY>'`
