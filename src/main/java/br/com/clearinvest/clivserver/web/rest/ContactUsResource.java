package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.service.MailService;
import br.com.clearinvest.clivserver.service.dto.ContactUsMessageDTO;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * REST controller for managing BrokerageAccount.
 */
@RestController
@RequestMapping("/api")
public class ContactUsResource {

    private final Logger log = LoggerFactory.getLogger(ContactUsResource.class);

    private final MailService mailService;

    public ContactUsResource(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * POST  /contact-us : Send a new message for the Cliv staff.
     *
     * @param ContactUsMessageDTO
     * @return the ResponseEntity with status 201 (Created), or with status 400 (Bad Request)
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-us")
    @Timed
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody ContactUsMessageDTO messageDTO) throws URISyntaxException {
        mailService.sendContactUsEmail(messageDTO);
        return ResponseEntity.ok(null);
    }

}
