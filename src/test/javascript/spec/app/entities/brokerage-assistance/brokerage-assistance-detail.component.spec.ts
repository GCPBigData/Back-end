/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageAssistanceDetailComponent } from 'app/entities/brokerage-assistance/brokerage-assistance-detail.component';
import { BrokerageAssistance } from 'app/shared/model/brokerage-assistance.model';

describe('Component Tests', () => {
    describe('BrokerageAssistance Management Detail Component', () => {
        let comp: BrokerageAssistanceDetailComponent;
        let fixture: ComponentFixture<BrokerageAssistanceDetailComponent>;
        const route = ({ data: of({ brokerageAssistance: new BrokerageAssistance(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageAssistanceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BrokerageAssistanceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageAssistanceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.brokerageAssistance).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
