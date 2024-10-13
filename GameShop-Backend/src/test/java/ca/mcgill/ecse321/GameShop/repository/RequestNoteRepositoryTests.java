package ca.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.GameShop.model.RequestNote;
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
    public void testPersistAndLoadRequestNote() {
        // Create a GameRequest (assuming the class exists and is required by RequestNote)
        GameRequest gameRequest = new GameRequest();
        gameRequest = gameRequestRepo.save(gameRequest);

        // Create an EmployeeAccount (concrete subclass of StaffAccount)
        EmployeeAccount staffAccount = new EmployeeAccount("test.email@example.com", "password", true);
        staffAccount = employeeAccountRepo.save(staffAccount);

        // Create RequestNote
        RequestNote note = new RequestNote("This is a test note.", Date.valueOf("2023-10-10"), gameRequest, staffAccount);

        // Save RequestNote
        note = requestNoterepo.save(note);
        int noteId = note.getNoteId();

        // Read RequestNote from the database
        RequestNote noteFromDb = requestNoterepo.findById(noteId).orElse(null);

        // Assert correct retrieval
        assertNotNull(noteFromDb);
        assertEquals(note.getContent(), noteFromDb.getContent());
        assertEquals(note.getNoteDate(), noteFromDb.getNoteDate());
        assertEquals(gameRequest.getGameEntityId(), noteFromDb.getGameRequest().getGameEntityId());
        assertEquals(staffAccount.getStaffId(), noteFromDb.getNotesWriter().getStaffId());
    }
}
