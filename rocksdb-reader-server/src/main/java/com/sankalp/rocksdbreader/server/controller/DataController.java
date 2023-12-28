package com.sankalp.rocksdbreader.server.controller;

import com.sankalp.rocksdbreader.server.service.RocksDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/dummy")
    public void addDummyData() {
        rocksDbService.addDummyData();
    }

}
