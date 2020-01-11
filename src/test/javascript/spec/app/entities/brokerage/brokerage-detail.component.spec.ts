/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageDetailComponent } from 'app/entities/brokerage/brokerage-detail.component';
import { Brokerage } from 'app/shared/model/brokerage.model';

describe('Component Tests', () => {
    describe('Brokerage Management Detail Component', () => {
        let comp: BrokerageDetailComponent;
        let fixture: ComponentFixture<BrokerageDetailComponent>;
        const route = ({ data: of({ brokerage: new Brokerage(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BrokerageDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.brokerage).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
