package com.example.crud.controller;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.News;
import com.example.crud.service.JwtService;
import com.example.crud.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsController {

    public static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    private NewsService newsService;
    private JwtService jwtService;

    public NewsController(NewsService newsService, JwtService jwtService){
        this.newsService= newsService;
        this.jwtService= jwtService;
    }

    @GetMapping(value = "/news")
    public ResponseEntity<News> getAllNews(){
        List<News> listNews= newsService.getListNews();
        return new ResponseEntity(listNews, HttpStatus.OK);
    }

    @PostMapping(value = "/news")
    public ResponseEntity<News> postNews(@RequestBody News news,
                                         HttpServletRequest request){
        if (jwtService.isAdmin(request)){
            news.setUser(jwtService.getCurrentUser(request));
            news.setDatePost(new Date().getTime());
            newsService.saveNews(news);
            return new ResponseEntity<>(news, HttpStatus.OK);
        }
        return new ResponseEntity("Bạn không phải admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @PutMapping(value = "/news/{id}")
    public ResponseEntity<News> updateNews(@RequestBody News news,
                                           @PathVariable(name = "id") long newsId,
                                           HttpServletRequest request){
        if(jwtService.isAdmin(request)){
            try {
                newsService.saveNews(news);
                return new ResponseEntity<>(news, HttpStatus.OK);
            }
            catch (Exception e){
                logger.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Bạn không phải admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @DeleteMapping(value = "/news/{id}")
    public ResponseEntity<News> deleteNews(@PathVariable(name = "id") long newsId,
                                           HttpServletRequest request){
        if(jwtService.isAdmin(request)){
            try{
                News news= newsService.getNewsById(newsId);
                newsService.deleteNews(news);
                return new ResponseEntity("Xóa bài viết thành công!", HttpStatus.OK);
            }
            catch (Exception e){
                logger.error(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity("Bạn không phải admin", HttpStatus.METHOD_NOT_ALLOWED);
    }
}
