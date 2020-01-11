/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageClientUpdateComponent } from 'app/entities/brokerage-client/brokerage-client-update.component';
import { BrokerageClientService } from 'app/entities/brokerage-client/brokerage-client.service';
import { BrokerageClient } from 'app/shared/model/brokerage-client.model';

describe('Component Tests', () => {
    describe('BrokerageClient Management Update Component', () => {
        let comp: BrokerageClientUpdateComponent;
        let fixture: ComponentFixture<BrokerageClientUpdateComponent>;
        let service: BrokerageClientService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageClientUpdateComponent]
            })
                .overrideTemplate(BrokerageClientUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BrokerageClientUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageClientService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageClient(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageClient = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageClient();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageClient = entity;
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
