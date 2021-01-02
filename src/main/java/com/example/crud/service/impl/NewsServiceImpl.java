package com.example.crud.service.impl;

import com.example.crud.entity.News;
import com.example.crud.repository.NewsRepository;
import com.example.crud.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> getListNews() {
        return (List<News>) newsRepository.findAll();
    }

    @Override
    public News getNewsById(long newsId) {
        return newsRepository.findById(newsId).get();
    }

    @Override
    public void deleteNews(News news) {
        newsRepository.delete(news);
    }

    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
    }

}
