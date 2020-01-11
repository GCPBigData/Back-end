/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageAssistanceDeleteDialogComponent } from 'app/entities/brokerage-assistance/brokerage-assistance-delete-dialog.component';
import { BrokerageAssistanceService } from 'app/entities/brokerage-assistance/brokerage-assistance.service';

describe('Component Tests', () => {
    describe('BrokerageAssistance Management Delete Component', () => {
        let comp: BrokerageAssistanceDeleteDialogComponent;
        let fixture: ComponentFixture<BrokerageAssistanceDeleteDialogComponent>;
        let service: BrokerageAssistanceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageAssistanceDeleteDialogComponent]
            })
                .overrideTemplate(BrokerageAssistanceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageAssistanceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageAssistanceService);
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
