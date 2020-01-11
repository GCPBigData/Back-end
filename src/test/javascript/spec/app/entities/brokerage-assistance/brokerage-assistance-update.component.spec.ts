/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageAssistanceUpdateComponent } from 'app/entities/brokerage-assistance/brokerage-assistance-update.component';
import { BrokerageAssistanceService } from 'app/entities/brokerage-assistance/brokerage-assistance.service';
import { BrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';

describe('Component Tests', () => {
    describe('BrokerageAssistance Management Update Component', () => {
        let comp: BrokerageAssistanceUpdateComponent;
        let fixture: ComponentFixture<BrokerageAssistanceUpdateComponent>;
        let service: BrokerageAssistanceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageAssistanceUpdateComponent]
            })
                .overrideTemplate(BrokerageAssistanceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BrokerageAssistanceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageAssistanceService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageAssistance(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageAssistance = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageAssistance();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageAssistance = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
