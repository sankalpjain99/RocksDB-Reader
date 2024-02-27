package com.sankalp.rocksdbreader.server.controller;

import com.sankalp.rocksdbreader.server.exception.InvalidColumnFamilyException;
import com.sankalp.rocksdbreader.server.exception.DataNotFoundException;
import com.sankalp.rocksdbreader.server.model.request.SearchKey;
import com.sankalp.rocksdbreader.server.service.RocksDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rocksdb/reader/v1/data")
@RequiredArgsConstructor
public class DataController {

    private final RocksDbService rocksDbService;

    @GetMapping("/columnFamilies")
    public List<String> getAllColumnFamilies() {
        return rocksDbService.getAllColumnFamilies();
    }

    @PostMapping(value = "/key", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getValueByKey(@RequestBody SearchKey searchKey) {
        try {
            return ResponseEntity.ok(rocksDbService.getValueByKey(searchKey));
        } catch (DataNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidColumnFamilyException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/dummy")
    public void addDummyData() {
        rocksDbService.addDummyData();
    }

}
