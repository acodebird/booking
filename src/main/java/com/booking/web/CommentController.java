package com.booking.web;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.domain.Comment;
import com.booking.domain.Hotel;
import com.booking.domain.Room;
import com.booking.dto.CommentQueryDTO;
import com.booking.service.CommentService;
import com.booking.utils.CopyPropertiesUtil;
import com.booking.utils.ResponseEntity;
import com.booking.utils.STablePageRequest;

@RestController
@RequestMapping(value = "/commentManage")
public class CommentController {
	@Autowired
	CommentService commentService;
	
	/**
     * 获取一页评论
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<Comment>> getHotelPage(STablePageRequest pageable, CommentQueryDTO commentQueryDTO) {
        if (StringUtils.isBlank(pageable.getSortField())) {
            pageable.setSortField("cid");
        }
        Page<Comment> page = Page.empty(pageable.getPageable());
        page = commentService.findAll(CommentQueryDTO.getSpecification(commentQueryDTO), pageable.getPageable());
        return ResponseEntity.ofSuccess().status(HttpStatus.OK).data(page);
    }
    
    /**
     * 根据id删除评论信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteHotelById(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }

    /**
     * 批量删除评论信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public ResponseEntity deleteHotelByIds(Long[] ids) {
        commentService.deleteAll(Arrays.asList(ids));
        return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
    /**
     * 回复评论
     * @param comment
     * @return
     */
    @PutMapping("/{cid}")
    public ResponseEntity reply(@PathVariable("cid") Long cid, @RequestBody Comment comment) {
    	Comment target = commentService.findById(cid);
        BeanUtils.copyProperties(comment, target, CopyPropertiesUtil.getNullPropertyNames(comment));
        commentService.save(target);
    	return ResponseEntity.ofSuccess().status(HttpStatus.OK);
    }
}
