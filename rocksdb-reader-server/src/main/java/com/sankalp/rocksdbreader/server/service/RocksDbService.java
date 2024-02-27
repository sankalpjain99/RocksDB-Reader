package com.sankalp.rocksdbreader.server.service;

import com.sankalp.rocksdbreader.server.exception.InvalidColumnFamilyException;
import com.sankalp.rocksdbreader.server.exception.DataNotFoundException;
import com.sankalp.rocksdbreader.server.model.request.SearchKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
                .toList();
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
        return columnFamiliesList;
    }

    public String getValueByKey(SearchKey searchKey) throws DataNotFoundException, InvalidColumnFamilyException {
        String columnFamilyName = searchKey.getColumnFamilyName();
        String key = searchKey.getKey();
        List<String> columnFamilies = getAllColumnFamilies();
        List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
        List<ColumnFamilyDescriptor> columnFamilyDescriptorList = columnFamilies.stream()
                .map(name -> new ColumnFamilyDescriptor(name.getBytes()))
                .toList();
        try(
            DBOptions dbOptions = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
            final RocksDB rocksDB = RocksDB.openReadOnly(dbOptions, rocksDbPath, columnFamilyDescriptorList, columnFamilyHandleList)
        ) {
            final ColumnFamilyHandle columnFamilyHandle = getColumnFamilyHandle(columnFamilyHandleList, columnFamilyName);
            if(Objects.isNull(columnFamilyHandle)) {
                throw new InvalidColumnFamilyException();
            }
            byte[] keyBytes = key.getBytes();
            byte[] valueBytes = rocksDB.get(columnFamilyHandle, keyBytes);
            if(Objects.isNull(valueBytes)) {
                log.error("No value found for key {} in column Family {}", key, columnFamilyName);
                throw new DataNotFoundException();
            } else {
                return new String(valueBytes);
            }
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDummyData() {
        List<ColumnFamilyDescriptor> columnFamilyDescriptors = Arrays.asList(
                new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY),
                new ColumnFamilyDescriptor("FAMILY_1".getBytes()),
                new ColumnFamilyDescriptor("FAMILY_2".getBytes()));
        DBOptions dbOptions = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
        List<ColumnFamilyHandle> columnFamilyHandleList = new ArrayList<>();
        try(RocksDB db = RocksDB.open(dbOptions, rocksDbPath, columnFamilyDescriptors, columnFamilyHandleList)) {
            db.put(getColumnFamilyHandle(columnFamilyHandleList, "FAMILY_1"), "Key1".getBytes(), "Value1".getBytes());
            db.put(getColumnFamilyHandle(columnFamilyHandleList, "FAMILY_2"), "Key2".getBytes(), "Value2".getBytes());
            log.info("Dummy Data Added Successfully!");
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        }
    }

    private ColumnFamilyHandle getColumnFamilyHandle(List<ColumnFamilyHandle> columnFamilyHandleList, String handleName) throws RocksDBException {
        for(ColumnFamilyHandle handle: columnFamilyHandleList) {
            if(new String(handle.getName()).equals(handleName)) {
                return handle;
            }
        }
        return null;
    }

}
