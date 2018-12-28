/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ClivServerTestModule } from '../../../test.module';
import { MarketSectorDeleteDialogComponent } from 'app/entities/market-sector/market-sector-delete-dialog.component';
import { MarketSectorService } from 'app/entities/market-sector/market-sector.service';

describe('Component Tests', () => {
    describe('MarketSector Management Delete Component', () => {
        let comp: MarketSectorDeleteDialogComponent;
        let fixture: ComponentFixture<MarketSectorDeleteDialogComponent>;
        let service: MarketSectorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [MarketSectorDeleteDialogComponent]
            })
                .overrideTemplate(MarketSectorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MarketSectorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MarketSectorService);
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
