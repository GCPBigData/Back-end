import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBrokerage } from 'app/shared/model/brokerage.model';
import { BrokerageService } from './brokerage.service';

@Component({
    selector: 'jhi-brokerage-delete-dialog',
    templateUrl: './brokerage-delete-dialog.component.html'
})
export class BrokerageDeleteDialogComponent {
    brokerage: IBrokerage;

    constructor(private brokerageService: BrokerageService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.brokerageService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'brokerageListModification',
                content: 'Deleted an brokerage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-brokerage-delete-popup',
    template: ''
})
export class BrokerageDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerage }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BrokerageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.brokerage = brokerage;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
