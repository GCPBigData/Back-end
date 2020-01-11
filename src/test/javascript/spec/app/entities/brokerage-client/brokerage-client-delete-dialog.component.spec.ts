/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageClientDeleteDialogComponent } from 'app/entities/brokerage-client/brokerage-client-delete-dialog.component';
import { BrokerageClientService } from 'app/entities/brokerage-client/brokerage-client.service';

describe('Component Tests', () => {
    describe('BrokerageClient Management Delete Component', () => {
        let comp: BrokerageClientDeleteDialogComponent;
        let fixture: ComponentFixture<BrokerageClientDeleteDialogComponent>;
        let service: BrokerageClientService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageClientDeleteDialogComponent]
            })
                .overrideTemplate(BrokerageClientDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageClientDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageClientService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
