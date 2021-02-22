package com.codegym.airbnb.controller;

import com.codegym.airbnb.model.Response;
import com.codegym.airbnb.model.Room;
import com.codegym.airbnb.security.JwtUtil;
import com.codegym.airbnb.services.HomeService;
import com.codegym.airbnb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/rooms")
@CrossOrigin("*")
public class HomeController {

    @Autowired
    private HomeService homeService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public Response home() {
        Response res = new Response();
        res.setData(homeService.findAll());
        // Fix lai trả về dữ liệu có phân trang.
        res.setMessage("SUCCESS");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    @GetMapping("{id}")
    public Response findById(@PathVariable("id") Long id) {
        Response res = new Response();
        Optional<Room> home = homeService.findById(id);
        res.setData(home.orElseGet(() -> null));
        if (home.isPresent()) {
            res.setMessage("SUCCESS");
            res.setStatus(HttpStatus.OK);
        } else {
            res.setMessage("No rooms available");
            res.setStatus(HttpStatus.NOT_FOUND);
        }
        return res;
    }

    @GetMapping("/city/{id}")
    public Response findByCityId(@PathVariable("id") int id) {
        Response res = new Response();
        ArrayList<Room> home = (ArrayList<Room>) homeService.findAllByCityId(id);
        res.setMessage("SUCCESS");
        res.setStatus(HttpStatus.OK);
        res.setData(home);
        return res;
    }

    @GetMapping("/address/{add}")
    public Response findByAddress(@PathVariable("add") String add) {
        Response res = new Response();
        ArrayList<Room> home = (ArrayList<Room>) homeService.findAllByAddress(add);
        res.setMessage("SUCCESS");
        res.setStatus(HttpStatus.OK);
        res.setData(home);
        return res;
    }

    @PostMapping
    public Response createPost(@RequestBody Room room, @RequestHeader(name = "Authorization") String tokenReq) {
        String token = tokenReq.substring(7);
        String userName = jwtUtil.extractUsername(token);
        Response res = new Response();
        room.setUser(userService.findByUserName(userName).get());
        room.setCreatedDate(LocalDateTime.now());
        room.setModifiedDate(LocalDateTime.now());
        res.setData(homeService.save(room));
        res.setMessage("SUCCESS");
        res.setStatus(HttpStatus.OK);
        return res;
    }

    @PutMapping("{id}/status")
    public void changeStatus(@PathVariable int id) {
        for (Room room : homeService.findAll()) {
            if (room.getId() == id) {
                room.setStatus(!room.isStatus());
                homeService.save(room);
                break;
            }
        }
    }

    @PutMapping("{id}/cancelled")
    public Response cancelled(@PathVariable("id") Long id, @RequestBody Room roomObj) {
        Optional<Room> room = homeService.findById(id);
        if (room.isPresent()) {
            room.get().setCancelled(roomObj.getCancelled());
            homeService.save(room.get());
            return new Response(room, "success", HttpStatus.OK);
        } else {
            return new Response(null, "Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
