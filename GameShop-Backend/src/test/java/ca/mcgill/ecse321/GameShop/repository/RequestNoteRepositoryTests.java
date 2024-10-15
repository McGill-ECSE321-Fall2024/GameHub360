package ca.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.RequestNote;
import jakarta.transaction.Transactional;
import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;
import ca.mcgill.ecse321.GameShop.model.GameRequest;

@SpringBootTest
public class RequestNoteRepositoryTests {

    @Autowired
    private RequestNoteRepository requestNoterepo;

    @Autowired
    private GameRequestRepository gameRequestRepo;

    @Autowired
    private EmployeeAccountRepository employeeAccountRepo;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        requestNoterepo.deleteAll();
        gameRequestRepo.deleteAll();
        employeeAccountRepo.deleteAll();
    }

    @Test
    @Transactional
    public void testPersistAndLoadRequestNote() {
        // Arrange
        GameRequest gameRequest = new GameRequest();
        gameRequest = gameRequestRepo.save(gameRequest);

        EmployeeAccount employeeAccount = new EmployeeAccount("test.email@example.com", "password", true);
        employeeAccount = employeeAccountRepo.save(employeeAccount);

        RequestNote note = new RequestNote("This is a test note.", Date.valueOf("2023-10-10"), gameRequest, employeeAccount);

        // Act
        note = requestNoterepo.save(note);
        int noteId = note.getNoteId();

        RequestNote noteFromDb = requestNoterepo.findRequestNoteByNoteId(noteId);

        // Assert
        assertNotNull(noteFromDb);
        assertEquals(noteId, noteFromDb.getNoteId());
        assertEquals(note.getContent(), noteFromDb.getContent());
        assertEquals(note.getNoteDate(), noteFromDb.getNoteDate());

        assertNotNull(noteFromDb.getGameRequest());
        assertEquals(gameRequest.getGameEntityId(), noteFromDb.getGameRequest().getGameEntityId());
        assertTrue(gameRequestRepo.findById(gameRequest.getGameEntityId()).get().getAssociatedNotes().contains(noteFromDb));
        
        assertNotNull(noteFromDb.getNotesWriter());
        assertEquals(employeeAccount.getStaffId(), noteFromDb.getNotesWriter().getStaffId());
        assertTrue(employeeAccountRepo.findById(employeeAccount.getStaffId()).get().getWrittenNotes().contains(noteFromDb));
    }
}
