/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClivServerTestModule } from '../../../test.module';
import { MarketSectorDetailComponent } from 'app/entities/market-sector/market-sector-detail.component';
import { MarketSector } from 'app/shared/model/market-sector.model';

describe('Component Tests', () => {
    describe('MarketSector Management Detail Component', () => {
        let comp: MarketSectorDetailComponent;
        let fixture: ComponentFixture<MarketSectorDetailComponent>;
        const route = ({ data: of({ marketSector: new MarketSector(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClivServerTestModule],
                declarations: [MarketSectorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(MarketSectorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MarketSectorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.marketSector).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
