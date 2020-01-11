/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { BrokerageClientDetailComponent } from 'app/entities/brokerage-client/brokerage-client-detail.component';
import { BrokerageClient } from 'app/shared/model/brokerage-client.model';

describe('Component Tests', () => {
    describe('BrokerageClient Management Detail Component', () => {
        let comp: BrokerageClientDetailComponent;
        let fixture: ComponentFixture<BrokerageClientDetailComponent>;
        const route = ({ data: of({ brokerageClient: new BrokerageClient(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [BrokerageClientDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BrokerageClientDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BrokerageClientDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.brokerageClient).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
