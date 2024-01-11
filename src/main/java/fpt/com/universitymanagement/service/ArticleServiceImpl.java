package fpt.com.universitymanagement.service;

import fpt.com.universitymanagement.dto.ArticleRequest;
import fpt.com.universitymanagement.dto.ArticleResponse;
import fpt.com.universitymanagement.entity.account.Article;
import fpt.com.universitymanagement.exception.BusinessException;
import fpt.com.universitymanagement.repository.ArticleRepository;
import fpt.com.universitymanagement.service.impl.ArticleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    
    private final ModelMapper modelMapper = new ModelMapper();
    
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    
    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleRequest request) {
        Optional<Article> existingArticle = articleRepository.findByTitle(request.getTitle());
        if (existingArticle.isPresent()) {
            throw new BusinessException("This title " + request.getTitle() + " already exists!");
        }
        Article article = modelMapper.map(request, Article.class);
        article = articleRepository.save(article);
        return modelMapper.map(article, ArticleResponse.class);
    }
    
    @Override
    public ArticleResponse getArticle(Long id) {
        return modelMapper.map(articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Article not found")), ArticleResponse.class);
    }
}
