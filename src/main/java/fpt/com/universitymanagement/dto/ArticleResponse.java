package fpt.com.universitymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponse extends BaseResponse {
    private long id;
    private String title;
    private String htmlContent;
}
