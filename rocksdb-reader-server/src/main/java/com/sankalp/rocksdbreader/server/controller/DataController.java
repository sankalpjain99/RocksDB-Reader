package com.sankalp.rocksdbreader.server.controller;

import com.sankalp.rocksdbreader.server.service.RocksDbService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/handle/{handleName}/key/{key}")
    public String getValueByKey(@PathVariable String handleName,
                                      @PathVariable String key) {
        return rocksDbService.getValueByKey(handleName, key);
    }

    @PostMapping("/dummy")
    public void addDummyData() {
        rocksDbService.addDummyData();
    }

}
