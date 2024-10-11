package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.RequestNote;

public interface RequestNoteRepository extends CrudRepository<RequestNote, Integer> {
    public RequestNote findRequestNoteByNoteId(Integer noteId);
}
