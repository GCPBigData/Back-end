/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageUpdateComponent } from 'app/entities/brokerage/brokerage-update.component';
import { BrokerageService } from 'app/entities/brokerage/brokerage.service';
import { Brokerage } from 'app/shared/model/brokerage.model';

describe('Component Tests', () => {
    describe('Brokerage Management Update Component', () => {
        let comp: BrokerageUpdateComponent;
        let fixture: ComponentFixture<BrokerageUpdateComponent>;
        let service: BrokerageService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageUpdateComponent]
            })
                .overrideTemplate(BrokerageUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BrokerageUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Brokerage(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerage = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Brokerage();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerage = entity;
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
