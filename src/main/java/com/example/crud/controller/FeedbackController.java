package com.example.crud.controller;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.*;
import com.example.crud.predicate.PredicateFeedback;
import com.example.crud.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FeedbackController {

    private UserService userService;
    private OrderService orderService;
    private ProductService productService;
    private JwtService jwtService;
    private FeedbackService feedbackService;

    public FeedbackController(JwtService jwtService, UserService userService, OrderService orderService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/userPage/feedbacks")
    public ResponseEntity<FeedBack> createFeedback(@RequestBody FeedBack feedBack, HttpServletRequest request) {
        if (jwtService.isCustomer(request)) {
            try {
                long userId = jwtService.getCurrentUser(request).getUserId();
                //check validator + permission
                Order order = orderService.findById(feedBack.getOrder().getOrderId());
                if (feedBack.getOrder().getUser().getUserId() != userId || !feedBack.getOrder().getStatus().equals(InputParam.FINISHED)) {
                    return new ResponseEntity("Not permit", HttpStatus.METHOD_NOT_ALLOWED);
                }
                feedbackService.save(feedBack);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);

    }

    @PutMapping(value = "/userPage/feedback/{feedback-id}")
    public ResponseEntity<FeedBack> updateFeedback(@PathVariable(name = "feedback-id") long feedbackId,
                                                   @RequestBody FeedBack feedBack, HttpServletRequest request) {
        if (jwtService.isCustomer(request)) {
            try {
                long userId = jwtService.getCurrentUser(request).getUserId();
                //check validator + permission
                Order order = orderService.findById(feedBack.getOrder().getOrderId());
                if (feedBack.getOrder().getUser().getUserId() != userId || !feedBack.getOrder().getStatus().equals(InputParam.FINISHED)) {
                    return new ResponseEntity("Not permit", HttpStatus.METHOD_NOT_ALLOWED);
                }
                if(feedbackId!= feedBack.getFeedbackId()){
                    return new ResponseEntity("Check your input", HttpStatus.BAD_REQUEST);
                }
                feedbackService.updateFeedback(feedBack);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/userPage/feedback")
    public ResponseEntity<FeedBack> deleteFeedback(@RequestBody FeedBack feedBack, HttpServletRequest request) {
        if (jwtService.isCustomer(request)) {
            try {
                long userId = jwtService.getCurrentUser(request).getUserId();
                //check validator + permission
                Order order = orderService.findById(feedBack.getOrder().getOrderId());
                if (feedBack.getOrder().getUser().getUserId() != userId || !feedBack.getOrder().getStatus().equals(InputParam.FINISHED)) {
                    return new ResponseEntity("Not permit", HttpStatus.METHOD_NOT_ALLOWED);
                }
                feedbackService.deleteFeedback(feedBack);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
    }

    //____________________________________ADMIN______________________________________


    @GetMapping(value = "/feedbacks")
    public ResponseEntity<FeedBack> getListFeedBack(@RequestParam(required = false, defaultValue = "10") int limit,
                                                    @RequestParam(required = false, defaultValue = "1") int page){
        try {
            List<FeedBack> feedBacks= feedbackService.findAll();
            feedBacks= feedbackService.sortByDatePost(feedBacks);
            Map<String, Object> result= new HashMap<>();
            result.put(InputParam.DATA, feedBacks);
            Map<String, Object> paging= new HashMap<>();
            paging.put(InputParam.TOTAL_PAGE, 3);
            paging.put(InputParam.TOTAL_COUNT, feedBacks.size());
            paging.put(InputParam.RECORD_IN_PAGE, limit);
            paging.put(InputParam.CURRENT_PAGE, page);
            result.put(InputParam.PAGING, paging);
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/adminPage/feedback")
    public ResponseEntity<FeedBack> deleteFeedbackByAdmin(@RequestBody FeedBack feedBack, HttpServletRequest request) {
        if (jwtService.isCustomer(request)) {
            try {
                long userId = jwtService.getCurrentUser(request).getUserId();
                //check validator + permission
                Order order = orderService.findById(feedBack.getOrder().getOrderId());
                if (feedBack.getOrder().getUser().getUserId() != userId || !feedBack.getOrder().getStatus().equals(InputParam.FINISHED)) {
                    return new ResponseEntity("Not permit", HttpStatus.METHOD_NOT_ALLOWED);
                }
                feedbackService.deleteFeedback(feedBack);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity("Bạn không phải admin", HttpStatus.METHOD_NOT_ALLOWED);
    }
}
