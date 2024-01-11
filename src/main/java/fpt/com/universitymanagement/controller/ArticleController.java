package fpt.com.universitymanagement.controller;

import fpt.com.universitymanagement.dto.ArticleRequest;
import fpt.com.universitymanagement.dto.ArticleResponse;
import fpt.com.universitymanagement.service.impl.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static fpt.com.universitymanagement.common.Constant.ARTICLE_CONTROLLER;

@RestController
@RequestMapping(ARTICLE_CONTROLLER)
public class ArticleController {
    ArticleService articleService;
    
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }
    
    @Operation(summary = "Fetch an article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArticleResponse.class)))
            })
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleResponse getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }
    
    @Operation(summary = "Create an article")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully!", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArticleResponse.class)))
            })
    })
    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse uploadArticle(@RequestBody ArticleRequest request) {
        return articleService.createArticle(request);
    }
}
