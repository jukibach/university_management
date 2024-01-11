package fpt.com.universitymanagement.service.impl;

import fpt.com.universitymanagement.dto.ArticleRequest;
import fpt.com.universitymanagement.dto.ArticleResponse;

public interface ArticleService {
    ArticleResponse createArticle(ArticleRequest request);
    
    ArticleResponse getArticle(Long id);
}
