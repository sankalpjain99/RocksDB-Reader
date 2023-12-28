package com.sankalp.rocksdbreader.server.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RocksDbService {

    @Value("${rocksdb.path}")
    private String rocksDbPath;

    public List<String> getAllColumnFamilies() {
        List<String> columnFamiliesList;
        try {
            List<byte[]> columnFamiliesByteList = RocksDB.listColumnFamilies(new Options(), rocksDbPath);
            columnFamiliesList = columnFamiliesByteList.stream()
                .map(String::new)
                .collect(Collectors.toList());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return columnFamiliesList;
    }

    public void addDummyData() {
        List<ColumnFamilyDescriptor> columnFamilyDescriptors = Arrays.asList(
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor("FAMILY_1".getBytes()),
                new ColumnFamilyDescriptor("FAMILY_2".getBytes()));
        List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
        DBOptions dbOptions = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
        try(RocksDB db = RocksDB.open(dbOptions, rocksDbPath, columnFamilyDescriptors, columnFamilyHandleList)) {
            db.put(getColumnFamilyHandle(columnFamilyHandleList, "FAMILY_1"), "Key1".getBytes(), "Value1".getBytes());
            db.put(getColumnFamilyHandle(columnFamilyHandleList, "FAMILY_2"), "Key2".getBytes(), "Value2".getBytes());
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        log.info("Data Added Successfully!");
    }

    @SneakyThrows
    private ColumnFamilyHandle getColumnFamilyHandle(List<ColumnFamilyHandle> columnFamilyHandleList, String handleName) {
        for(ColumnFamilyHandle handle: columnFamilyHandleList) {
            if(new String(handle.getName()).equals(handleName)) {
                return handle;
            }
        }
        return null;
    }

}
