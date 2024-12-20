package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.Reply;

public interface ReplyRepository extends CrudRepository<Reply, Integer> {
    // Find reply by reply id
    public Reply findReplyByReplyId(Integer replyId);
}
