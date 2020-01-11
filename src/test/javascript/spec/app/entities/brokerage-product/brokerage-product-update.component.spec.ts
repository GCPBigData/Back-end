/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageProductUpdateComponent } from 'app/entities/brokerage-product/brokerage-product-update.component';
import { BrokerageProductService } from 'app/entities/brokerage-product/brokerage-product.service';
import { BrokerageProduct } from 'app/shared/model/brokerage-product.model';

describe('Component Tests', () => {
    describe('BrokerageProduct Management Update Component', () => {
        let comp: BrokerageProductUpdateComponent;
        let fixture: ComponentFixture<BrokerageProductUpdateComponent>;
        let service: BrokerageProductService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageProductUpdateComponent]
            })
                .overrideTemplate(BrokerageProductUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BrokerageProductUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BrokerageProductService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageProduct(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageProduct = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BrokerageProduct();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.brokerageProduct = entity;
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
