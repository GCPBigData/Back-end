/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageProductDetailComponent } from 'app/entities/brokerage-product/brokerage-product-detail.component';
import { BrokerageProduct } from 'app/shared/model/brokerage-product.model';

describe('Component Tests', () => {
    describe('BrokerageProduct Management Detail Component', () => {
        let comp: BrokerageProductDetailComponent;
        let fixture: ComponentFixture<BrokerageProductDetailComponent>;
        const route = ({ data: of({ brokerageProduct: new BrokerageProduct(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageProductDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BrokerageProductDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageProductDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.brokerageProduct).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
