package com.example.crud.service;

import com.example.crud.entity.News;

import java.util.List;

public interface NewsService {
    List<News> getListNews();
    News getNewsById(long newsId);
    void deleteNews(News newsId);
    void saveNews(News news);
}
